from fluentstructure import Card, FrenchDeck, spades_high

deck = FrenchDeck()

print(len(deck))

for card in sorted(deck, key=spades_high):
  print(card)
