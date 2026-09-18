from django.db import models
from django.conf import settings
from apps.annonces.models import Annonce

class Discussion(models.Model):
    annonce = models.OneToOneField(Annonce, on_delete=models.CASCADE, related_name='discussion', null=True, blank=True)
    
class UserDiscussion(models.Model):
    user = models.ForeignKey(settings.AUTH_USER_MODEL, on_delete=models.CASCADE)
    discussion = models.ForeignKey(Discussion, on_delete=models.CASCADE)
    
    class Meta:
        unique_together = ('user', 'discussion')
        
class Proposition(models.Model):
    STATUS_CHOICES = [
        ("pending", "En attente"),
        ("accepted", "Acceptée"),
        ("rejected", "Rejetée"),
    ]
    discussion = models.ForeignKey(Discussion, on_delete=models.CASCADE, related_name='propositions')
    user = models.ForeignKey(settings.AUTH_USER_MODEL, on_delete=models.CASCADE)
    status = models.CharField(max_length=20, choices=STATUS_CHOICES, default="pending")
 
class Message(models.Model):
    discussion = models.ForeignKey(Discussion, on_delete=models.CASCADE, related_name='messages')
    user = models.ForeignKey(settings.AUTH_USER_MODEL, on_delete=models.CASCADE)
    content = models.TextField()
    timestamp = models.DateTimeField(auto_now_add=True)