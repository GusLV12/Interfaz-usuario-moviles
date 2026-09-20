import 'package:flutter/widgets.dart' show Key, ListView, Scrollable;
import 'package:flutter_test/flutter_test.dart';

import 'package:catalogo_ui/main.dart';

void main() {
  testWidgets('Inicio muestra las seis secciones del catálogo', (
    WidgetTester tester,
  ) async {
    await tester.pumpWidget(const CatalogApp());

    expect(find.text('Entrada de texto'), findsOneWidget);
    expect(find.text('Botones y acciones'), findsOneWidget);
    expect(find.text('Elementos de selección'), findsOneWidget);
    expect(find.text('Listas y colecciones'), findsOneWidget);

    await tester.scrollUntilVisible(
      find.text('Información y retroalimentación'),
      300,
      scrollable: find.byType(Scrollable),
    );
    expect(find.text('Información y retroalimentación'), findsOneWidget);

    await tester.scrollUntilVisible(
      find.text('Contenedores y estructura'),
      300,
      scrollable: find.byType(Scrollable),
    );
    expect(find.text('Contenedores y estructura'), findsOneWidget);
  });

  testWidgets('un texto válido llega desde Entrada hasta Colecciones', (
    WidgetTester tester,
  ) async {
    await tester.pumpWidget(const CatalogApp());

    await tester.tap(find.text('Entrada de texto'));
    await tester.pumpAndSettle();
    await tester.dragUntilVisible(
      find.byKey(const Key('pending_text_input')),
      find.byType(ListView),
      const Offset(0, -300),
    );
    await tester.enterText(
      find.byKey(const Key('pending_text_input')),
      'Elemento de prueba',
    );
    await tester.tap(find.byKey(const Key('add_to_collection_button')));
    await tester.pump();
    expect(find.text('Elemento agregado a la colección.'), findsOneWidget);

    await tester.tap(find.text('Listas'));
    await tester.pumpAndSettle();
    await tester.dragUntilVisible(
      find.text('Elemento de prueba'),
      find.byType(ListView),
      const Offset(0, -300),
    );
    expect(find.text('Elemento de prueba'), findsOneWidget);
  });
}
