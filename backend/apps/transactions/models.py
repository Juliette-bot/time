from django.db import models
from django.conf import settings
from apps.discussions.models import Proposition

class TransactionTime(models.Model):
    STATUS_CHOICES = [
        ("rejected", "Rejetée"),
        ("validated", "Terminée"),
    ]
    proposition = models.OneToOneField(Proposition, on_delete=models.CASCADE, related_name='transaction')
    status = models.CharField(max_length=20, choices=STATUS_CHOICES)
    duration_minute = models.DurationField()
    creditor = models.ForeignKey(settings.AUTH_USER_MODEL, on_delete=models.CASCADE, related_name='creditor_transactions')
    debtor = models.ForeignKey(settings.AUTH_USER_MODEL, on_delete=models.CASCADE, related_name='debtor_transactions')
    completed = models.BooleanField(default=False)
    completed_at = models.DateTimeField(null=True, blank=True)

