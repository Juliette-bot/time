# Create your tests here.
from django.contrib.auth import get_user_model
from django.test import TestCase

from apps.annonces.models import Annonce
from apps.annonces.service import search_annonces


class SearchAnnoncesTests(TestCase):
    """Vérifie la recherche floue (pg_trgm) sur le titre des annonces."""

    @classmethod
    def setUpTestData(cls):
        user = get_user_model().objects.create_user(username="karima", password="test-pass-123")
        Annonce.objects.create(
            title="Cours de guitare", description="Débutants bienvenus", user=user
        )
        Annonce.objects.create(title="Aide au jardinage", description="Tonte et taille", user=user)

    def test_trouve_une_annonce_malgre_une_faute_de_frappe(self):
        resultats = search_annonces("guitarre")
        self.assertEqual([a.title for a in resultats], ["Cours de guitare"])

    def test_ne_renvoie_rien_si_aucune_annonce_ne_ressemble(self):
        self.assertFalse(search_annonces("plomberie").exists())