import 'package:flutter/widgets.dart' show Scrollable;
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
}
