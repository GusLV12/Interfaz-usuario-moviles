import 'package:flutter/material.dart';

import 'catalog_section.dart';

class HomeScreen extends StatelessWidget {
  const HomeScreen({required this.onSelect, super.key});

  final ValueChanged<CatalogSection> onSelect;

  @override
  Widget build(BuildContext context) {
    return ListView(
      padding: const EdgeInsets.all(16),
      children: [
        Text(
          'Explora componentes móviles',
          style: Theme.of(context).textTheme.headlineSmall,
        ),
        const SizedBox(height: 6),
        Text(
          'Cada sección explica un patrón y permite probarlo.',
          style: Theme.of(context).textTheme.bodyLarge,
        ),
        const SizedBox(height: 16),
        for (final section in CatalogSection.values.where(
          (section) => section != CatalogSection.home,
        ))
          Padding(
            padding: const EdgeInsets.only(bottom: 10),
            child: Card(
              clipBehavior: Clip.antiAlias,
              child: ListTile(
                leading: Icon(section.icon),
                title: Text(section.title),
                subtitle: Text(section.description),
                trailing: const Icon(Icons.chevron_right),
                onTap: () => onSelect(section),
              ),
            ),
          ),
      ],
    );
  }
}
