import 'package:flutter/material.dart';

import 'catalog_section.dart';

class SectionPlaceholder extends StatelessWidget {
  const SectionPlaceholder({required this.section, super.key});

  final CatalogSection section;

  @override
  Widget build(BuildContext context) {
    return Center(
      child: Padding(
        padding: const EdgeInsets.all(24),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            Icon(section.icon, size: 56),
            const SizedBox(height: 12),
            Text(
              section.title,
              style: Theme.of(context).textTheme.headlineSmall,
              textAlign: TextAlign.center,
            ),
            const SizedBox(height: 8),
            Text(section.description, textAlign: TextAlign.center),
          ],
        ),
      ),
    );
  }
}
