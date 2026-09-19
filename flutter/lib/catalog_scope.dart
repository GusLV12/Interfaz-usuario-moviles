import 'package:flutter/widgets.dart';

import 'catalog_store.dart';

class CatalogScope extends InheritedNotifier<CatalogStore> {
  const CatalogScope({
    required CatalogStore super.notifier,
    required super.child,
    super.key,
  });

  static CatalogStore of(BuildContext context) {
    final scope = context.dependOnInheritedWidgetOfExactType<CatalogScope>();
    assert(scope != null, 'CatalogScope no está disponible en este contexto.');
    return scope!.notifier!;
  }
}
