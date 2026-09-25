from django.contrib.postgres.search import TrigramSimilarity

from apps.annonces.models import Annonce


def search_annonces(query):
    return (
        Annonce.objects.annotate(similarity=TrigramSimilarity("title", query))
        .filter(similarity__gt=0.2)
        .order_by("-similarity")
    )
