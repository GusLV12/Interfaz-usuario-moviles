import 'package:flutter/foundation.dart';

class CatalogItem {
  const CatalogItem({
    required this.id,
    required this.title,
    required this.category,
  });

  final int id;
  final String title;
  final String category;
}

class CatalogStore extends ChangeNotifier {
  CatalogStore() : _items = defaultItems();

  String _pendingText = '';
  List<CatalogItem> _items;

  String get pendingText => _pendingText;
  List<CatalogItem> get items => List.unmodifiable(_items);

  void updatePendingText(String value) {
    _pendingText = value;
    notifyListeners();
  }

  bool addPendingText() {
    final value = _pendingText.trim();
    if (value.length < 3) return false;
    final nextId =
        _items.fold(
          0,
          (highest, item) => item.id > highest ? item.id : highest,
        ) +
        1;
    _items = [
      ..._items,
      CatalogItem(id: nextId, title: value, category: 'Agregado'),
    ];
    _pendingText = '';
    notifyListeners();
    return true;
  }

  CatalogItem? remove(int id) {
    final index = _items.indexWhere((item) => item.id == id);
    if (index == -1) return null;
    final removed = _items[index];
    _items = [..._items]..removeAt(index);
    notifyListeners();
    return removed;
  }

  void restore(CatalogItem item) {
    if (_items.any((entry) => entry.id == item.id)) return;
    _items = [..._items, item]..sort((a, b) => a.id.compareTo(b.id));
    notifyListeners();
  }

  void clear() {
    _items = [];
    notifyListeners();
  }

  void reset() {
    _items = defaultItems();
    notifyListeners();
  }
}

List<CatalogItem> defaultItems() => List.generate(
  15,
  (index) => CatalogItem(
    id: index + 1,
    title: 'Elemento ${index + 1}',
    category: index.isEven ? 'Diseño' : 'Interacción',
  ),
);
