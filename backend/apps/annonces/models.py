from django.conf import settings
from django.db import models


class Annonce(models.Model):
    STATUS_CHOICES = [
        ("pending", "En attente"),
        ("active", "Active"),
        ("closed", "Clorturée"),
    ]

    title = models.CharField(max_length=255)
    description = models.TextField()
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    status = models.CharField(max_length=20, choices=STATUS_CHOICES, default="pending")
    user = models.ForeignKey(
        settings.AUTH_USER_MODEL, on_delete=models.CASCADE, related_name="annonces"
    )
    user_request = models.ForeignKey(
        settings.AUTH_USER_MODEL,
        on_delete=models.SET_NULL,
        null=True,
        blank=True,
        related_name="requested_annonces",
    )
    duration = models.DurationField(null=True, blank=True)
    type_annonce = models.BooleanField(default=True)  # True for offer, False for request
