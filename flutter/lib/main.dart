import 'package:flutter/material.dart';

import 'catalog_scope.dart';
import 'catalog_section.dart';
import 'catalog_store.dart';
import 'home_screen.dart';
import 'section_placeholder.dart';

void main() => runApp(const CatalogApp());

class CatalogApp extends StatefulWidget {
  const CatalogApp({super.key});

  @override
  State<CatalogApp> createState() => _CatalogAppState();
}

class _CatalogAppState extends State<CatalogApp> {
  final CatalogStore _store = CatalogStore();
  CatalogSection _section = CatalogSection.home;

  @override
  void dispose() {
    _store.dispose();
    super.dispose();
  }

  void _showSection(CatalogSection section) =>
      setState(() => _section = section);

  @override
  Widget build(BuildContext context) {
    return CatalogScope(
      notifier: _store,
      child: MaterialApp(
        debugShowCheckedModeBanner: false,
        title: 'Catálogo UI',
        theme: ThemeData(
          colorScheme: ColorScheme.fromSeed(seedColor: const Color(0xff006d77)),
          useMaterial3: true,
        ),
        darkTheme: ThemeData(
          colorScheme: ColorScheme.fromSeed(
            seedColor: const Color(0xff52b6c0),
            brightness: Brightness.dark,
          ),
          useMaterial3: true,
        ),
        themeMode: ThemeMode.system,
        home: Scaffold(
          appBar: AppBar(
            title: Text(_section.title),
            leading: _section == CatalogSection.home
                ? null
                : IconButton(
                    icon: const Icon(Icons.arrow_back),
                    tooltip: 'Volver al inicio',
                    onPressed: () => _showSection(CatalogSection.home),
                  ),
          ),
          body: _section == CatalogSection.home
              ? HomeScreen(onSelect: _showSection)
              : SectionPlaceholder(section: _section),
          bottomNavigationBar: NavigationBar(
            selectedIndex: _bottomIndex,
            onDestinationSelected: (index) =>
                _showSection(bottomSections[index]),
            destinations: [
              for (final section in bottomSections)
                NavigationDestination(
                  icon: Icon(section.icon),
                  label: section.shortTitle,
                ),
            ],
          ),
        ),
      ),
    );
  }

  int get _bottomIndex {
    final index = bottomSections.indexOf(_section);
    return index == -1 ? 0 : index;
  }
}
