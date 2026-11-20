-- =====================================================================
-- Échange de Temps — demo data (fake users, listings, one full exchange)
-- Loaded automatically in dev.
-- In prod, ONLY if the image is built with
--   FLYWAY_LOCATIONS=db/migration,db/demo-data
-- NEVER enable it on a platform with real users: every account below,
-- including the admin, has the password: password123
-- Safe to run several times: does nothing if demo data already exists.
-- =====================================================================

DO $demo$
BEGIN
IF EXISTS (SELECT 1 FROM users WHERE email = 'admin@echange-temps.test') THEN
    RAISE NOTICE 'Demo data already present, skipping.';
    RETURN;
END IF;

-- Users
INSERT INTO users (email, password_hash, first_name, last_name, role, postal_code) VALUES
    ('admin@echange-temps.test',  '$2a$10$BR0itTEwP7nsFeB81L0oMugm69X9/RNy2ubK2hJ8gyU8cYmx26sb.', 'Admin',  'Démo',     'admin', '69009'),
    ('karima@echange-temps.test', '$2a$10$BR0itTEwP7nsFeB81L0oMugm69X9/RNy2ubK2hJ8gyU8cYmx26sb.', 'Karima', 'Benali',   'user',  '69007'),
    ('lucas@echange-temps.test',  '$2a$10$BR0itTEwP7nsFeB81L0oMugm69X9/RNy2ubK2hJ8gyU8cYmx26sb.', 'Lucas',  'Martin',   'user',  '69003'),
    ('ines@echange-temps.test',   '$2a$10$BR0itTEwP7nsFeB81L0oMugm69X9/RNy2ubK2hJ8gyU8cYmx26sb.', 'Inès',   'Dupont',   'user',  '69100'),
    ('marc@echange-temps.test',   '$2a$10$BR0itTEwP7nsFeB81L0oMugm69X9/RNy2ubK2hJ8gyU8cYmx26sb.', 'Marc',   'Lefebvre', 'user',  '01000');

-- Listings
INSERT INTO listing (title, description, type, status, duration_minutes, author_id) VALUES
    ('Cours de menuiserie',
     'Je peux t''apprendre les bases de la menuiserie : prendre des mesures, scier, poncer et finir une petite étagère.',
     'offer', 'active', 120, (SELECT id FROM users WHERE email = 'karima@echange-temps.test')),
    ('Aide pour un déménagement',
     'Je déménage samedi prochain et j''ai besoin d''un coup de main pour porter un canapé et une armoire.',
     'request', 'active', 90, (SELECT id FROM users WHERE email = 'lucas@echange-temps.test')),
    ('Conversation en espagnol',
     'Hispanophone de naissance, je te propose une heure de discussion en espagnol pour pratiquer.',
     'offer', 'active', 60, (SELECT id FROM users WHERE email = 'ines@echange-temps.test')),
    ('Réparation de vélo',
     'Je répare les crevaisons, je règle les freins et les vitesses. Passe chez moi avec ton vélo.',
     'offer', 'pending', 45, (SELECT id FROM users WHERE email = 'marc@echange-temps.test')),
    ('Arrosage des plantes pendant les vacances',
     'Je cherche quelqu''un pour arroser mes plantes deux fois par semaine en août.',
     'request', 'closed', 30, (SELECT id FROM users WHERE email = 'karima@echange-temps.test'));

-- Lucas answered Karima's woodworking lesson
UPDATE listing
SET requester_id = (SELECT id FROM users WHERE email = 'lucas@echange-temps.test')
WHERE title = 'Cours de menuiserie';

-- A full exchange: discussion -> messages -> proposal -> transaction
INSERT INTO discussion (listing_id)
VALUES ((SELECT id FROM listing WHERE title = 'Cours de menuiserie'));

INSERT INTO discussion_participant (user_id, discussion_id)
SELECT u.id, d.id
FROM users u, discussion d
JOIN listing l ON l.id = d.listing_id
WHERE l.title = 'Cours de menuiserie'
  AND u.email IN ('karima@echange-temps.test', 'lucas@echange-temps.test');

INSERT INTO message (discussion_id, author_id, content, sent_at)
SELECT d.id, u.id, m.content, now() - m.ago
FROM (VALUES
        ('lucas@echange-temps.test',  'Salut Karima ! J''aimerais beaucoup apprendre la menuiserie. Tu es dispo samedi ?', interval '3 days'),
        ('karima@echange-temps.test', 'Salut Lucas, samedi 14h ça me va. Rejoins-moi à l''atelier !',                      interval '2 days'),
        ('lucas@echange-temps.test',  'Parfait, à samedi. Merci !',                                                          interval '2 days' - interval '1 hour')
     ) AS m(email, content, ago)
JOIN users u ON u.email = m.email
CROSS JOIN discussion d
JOIN listing l ON l.id = d.listing_id
WHERE l.title = 'Cours de menuiserie';

INSERT INTO proposal (discussion_id, author_id, status)
SELECT d.id, u.id, 'accepted'
FROM discussion d
JOIN listing l ON l.id = d.listing_id
JOIN users u ON u.email = 'lucas@echange-temps.test'
WHERE l.title = 'Cours de menuiserie';

-- Karima gave 2 hours: she is credited, Lucas is debited
INSERT INTO time_transaction (proposal_id, status, duration_minutes, creditor_id, debtor_id, completed, completed_at)
SELECT p.id, 'validated', 120,
       (SELECT id FROM users WHERE email = 'karima@echange-temps.test'),
       (SELECT id FROM users WHERE email = 'lucas@echange-temps.test'),
       TRUE, now() - interval '1 day'
FROM proposal p
JOIN discussion d ON d.id = p.discussion_id
JOIN listing l ON l.id = d.listing_id
WHERE l.title = 'Cours de menuiserie';

END
$demo$;
