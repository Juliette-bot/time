from django.contrib.auth.models import AbstractUser
from django.db import models


class User(AbstractUser):
    ROLE_CHOICES = [
        ("user", "Utilisateur"),
        ("admin", "Administrateur"),
    ]
    role = models.CharField(max_length=20, choices=ROLE_CHOICES, default="user")
    code_postal = models.IntegerField(null=True, blank=True)
    # first_name, last_name, email, created (via date_joined) déjà fournis par AbstractUser
