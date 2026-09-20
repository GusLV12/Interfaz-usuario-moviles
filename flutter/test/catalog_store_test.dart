import 'package:flutter_test/flutter_test.dart';

import 'package:catalogo_ui/catalog_store.dart';

void main() {
  test('rechaza texto corto y conserva los elementos', () {
    final store = CatalogStore();
    final initialCount = store.items.length;

    store.updatePendingText('ab');

    expect(store.addPendingText(), isFalse);
    expect(store.items, hasLength(initialCount));
  });

  test('agrega texto válido y limpia el valor pendiente', () {
    final store = CatalogStore();

    store.updatePendingText('Nueva tarjeta');

    expect(store.addPendingText(), isTrue);
    expect(store.pendingText, isEmpty);
    expect(store.items.last.title, 'Nueva tarjeta');
    expect(store.items.last.category, 'Agregado');
  });

  test('elimina, restaura y reinicia los elementos', () {
    final store = CatalogStore();
    final removed = store.remove(1);

    expect(removed?.title, 'Elemento 1');
    expect(store.items.where((item) => item.id == 1), isEmpty);

    store.restore(removed!);
    expect(store.items.first.title, 'Elemento 1');

    store.clear();
    expect(store.items, isEmpty);
    store.reset();
    expect(store.items, hasLength(15));
  });
}
