import 'dart:async';

import 'package:flutter/material.dart';

import 'catalog_widgets.dart';

class FeedbackScreen extends StatefulWidget {
  const FeedbackScreen({super.key});

  @override
  State<FeedbackScreen> createState() => _FeedbackScreenState();
}

class _FeedbackScreenState extends State<FeedbackScreen> {
  String _status = 'Sin cambios';

  void _showToast() {
    final overlay = Overlay.of(context);
    late OverlayEntry entry;
    entry = OverlayEntry(
      builder: (context) => Positioned(
        bottom: 92,
        left: 24,
        right: 24,
        child: Material(
          color: Colors.transparent,
          child: Semantics(
            liveRegion: true,
            child: DecoratedBox(
              decoration: BoxDecoration(
                color: Theme.of(context).colorScheme.inverseSurface,
                borderRadius: BorderRadius.circular(6),
              ),
              child: Padding(
                padding: const EdgeInsets.all(14),
                child: Text(
                  'Toast mostrado',
                  style: TextStyle(
                    color: Theme.of(context).colorScheme.onInverseSurface,
                  ),
                ),
              ),
            ),
          ),
        ),
      ),
    );
    overlay.insert(entry);
    Future<void>.delayed(const Duration(seconds: 2), entry.remove);
  }

  @override
  Widget build(BuildContext context) {
    return CatalogPage(
      children: [
        Text(
          'Los mensajes, indicadores y superficies ayudan a explicar el estado de una acción.',
          style: Theme.of(context).textTheme.bodyLarge,
        ),
        const SizedBox(height: 14),
        DemoSection(
          title: 'Tipografía',
          description: 'Los estilos, tamaños y énfasis crean una jerarquía fácil de recorrer.',
          child: const Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(
                'Título destacado',
                style: TextStyle(fontSize: 24, fontWeight: FontWeight.bold),
              ),
              Text('Texto de cuerpo para explicar una decisión de interfaz.'),
              Text('Nota secundaria', style: TextStyle(fontSize: 12)),
            ],
          ),
        ),
        DemoSection(
          title: 'Imágenes',
          description: 'Se muestran una imagen del proyecto y otra cargada mediante URL, con escalado y fallos controlados.',
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              ClipRRect(
                borderRadius: BorderRadius.circular(6),
                child: Image.asset(
                  'assets/images/catalog_ui_local.png',
                  height: 120,
                  width: 120,
                  fit: BoxFit.cover,
                  semanticLabel: 'Ilustración local de componentes de interfaz',
                ),
              ),
              const SizedBox(height: 10),
              ClipRRect(
                borderRadius: BorderRadius.circular(6),
                child: Image.network(
                  'https://images.unsplash.com/photo-1551650975-87deedd944c3?auto=format&fit=crop&w=640&q=80',
                  height: 150,
                  width: double.infinity,
                  fit: BoxFit.cover,
                  semanticLabel: 'Imagen remota de una interfaz',
                  loadingBuilder: (context, child, progress) => progress == null
                      ? child
                      : const SizedBox(
                          height: 150,
                          child: Center(child: CircularProgressIndicator()),
                        ),
                  errorBuilder: (context, error, stackTrace) => const SizedBox(
                    height: 150,
                    child: Center(
                      child: Text('No se pudo cargar la imagen remota.'),
                    ),
                  ),
                ),
              ),
            ],
          ),
        ),
        DemoSection(
          title: 'Indicadores de progreso',
          description: 'Comunican un avance conocido o una actividad cuya duración no se puede calcular.',
          child: const Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text('Progreso determinado: 62 %'),
              SizedBox(height: 6),
              LinearProgressIndicator(value: .62),
              SizedBox(height: 12),
              LinearProgressIndicator(),
              SizedBox(height: 16),
              Row(
                children: [
                  CircularProgressIndicator(value: .62),
                  SizedBox(width: 20),
                  CircularProgressIndicator(),
                ],
              ),
            ],
          ),
        ),
        DemoSection(
          title: 'Mensajes',
          description: 'Toast y snackbar ofrecen confirmación breve; el snackbar incluye una acción que cambia el estado.',
          child: Wrap(
            spacing: 8,
            runSpacing: 8,
            children: [
              FilledButton(
                onPressed: _showToast,
                child: const Text('Mostrar toast'),
              ),
              OutlinedButton(
                onPressed: () {
                  ScaffoldMessenger.of(context).showSnackBar(
                    SnackBar(
                      content: const Text('Elemento guardado.'),
                      action: SnackBarAction(
                        label: 'Deshacer',
                        onPressed: () =>
                            setState(() => _status = 'Cambio deshecho'),
                      ),
                    ),
                  );
                },
                child: const Text('Mostrar snackbar'),
              ),
              Text(_status),
            ],
          ),
        ),
        DemoSection(
          title: 'Diálogo y hoja inferior',
          description: 'Solicitan confirmación o muestran contenido contextual sin salir de la pantalla.',
          child: Wrap(
            spacing: 8,
            runSpacing: 8,
            children: [
              FilledButton(
                onPressed: () => showDialog<void>(
                  context: context,
                  builder: (dialogContext) => AlertDialog(
                    title: const Text('¿Confirmar acción?'),
                    content: const Text(
                      'Esta demostración muestra un diálogo de confirmación.',
                    ),
                    actions: [
                      TextButton(
                        onPressed: () => Navigator.pop(dialogContext),
                        child: const Text('Cancelar'),
                      ),
                      FilledButton(
                        onPressed: () {
                          setState(() => _status = 'Acción confirmada');
                          Navigator.pop(dialogContext);
                        },
                        child: const Text('Aceptar'),
                      ),
                    ],
                  ),
                ),
                child: const Text('Confirmar'),
              ),
              OutlinedButton(
                onPressed: () => showModalBottomSheet<void>(
                  context: context,
                  showDragHandle: true,
                  builder: (sheetContext) => Padding(
                    padding: const EdgeInsets.fromLTRB(24, 0, 24, 28),
                    child: Column(
                      mainAxisSize: MainAxisSize.min,
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(
                          'Hoja inferior',
                          style: Theme.of(context).textTheme.titleLarge,
                        ),
                        const SizedBox(height: 8),
                        const Text(
                          'Este contenido aparece desde la parte inferior y se puede descartar.',
                        ),
                        const SizedBox(height: 12),
                        FilledButton(
                          onPressed: () => Navigator.pop(sheetContext),
                          child: const Text('Cerrar'),
                        ),
                      ],
                    ),
                  ),
                ),
                child: const Text('Abrir hoja'),
              ),
            ],
          ),
        ),
        DemoSection(
          title: 'Tarjeta, separador y distintivo',
          description:
              'Organizan información y resaltan cantidades pendientes.',
          child: Column(
            children: [
              Row(
                children: [
                  const Expanded(child: Text('Notificaciones')),
                  Badge(
                    label: const Text('3'),
                    child: IconButton(
                      tooltip: 'Bandeja',
                      onPressed: () =>
                          setState(() => _status = 'Bandeja revisada'),
                      icon: const Icon(Icons.inbox_outlined),
                    ),
                  ),
                ],
              ),
              const Divider(),
              const Align(
                alignment: Alignment.centerLeft,
                child: Text(
                  'La tarjeta contiene contenido relacionado en una superficie compartida.',
                ),
              ),
            ],
          ),
        ),
      ],
    );
  }
}

class LayoutScreen extends StatelessWidget {
  const LayoutScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return CatalogPage(
      children: [
        Text(
          'La barra superior y la navegación inferior que rodean esta pantalla forman parte de la estructura global.',
          style: Theme.of(context).textTheme.bodyLarge,
        ),
        const SizedBox(height: 14),
        DemoSection(
          title: 'Fila',
          description: 'Row organiza componentes de forma horizontal.',
          child: const Row(
            children: [
              Expanded(
                child: _ColorBlock(label: 'A', color: Color(0xff006d77)),
              ),
              SizedBox(width: 8),
              Expanded(
                child: _ColorBlock(label: 'B', color: Color(0xffd65a50)),
              ),
              SizedBox(width: 8),
              Expanded(
                child: _ColorBlock(label: 'C', color: Color(0xff315e9e)),
              ),
            ],
          ),
        ),
        DemoSection(
          title: 'Columna',
          description: 'Column apila componentes en el orden de lectura.',
          child: const Column(
            children: [
              _ColorBlock(label: 'Primer elemento', color: Color(0xff006d77)),
              SizedBox(height: 8),
              _ColorBlock(label: 'Segundo elemento', color: Color(0xff315e9e)),
            ],
          ),
        ),
        DemoSection(
          title: 'Superposición',
          description: 'Stack permite ubicar elementos uno encima de otro.',
          child: Container(
            height: 120,
            color: Theme.of(context).colorScheme.primaryContainer,
            child: Stack(
              children: [
                const Center(child: Text('Fondo')),
                Positioned(
                  top: 12,
                  right: 12,
                  child: Badge(
                    label: const Text('Nuevo'),
                    child: Icon(
                      Icons.notifications_outlined,
                      color: Theme.of(context).colorScheme.onPrimaryContainer,
                    ),
                  ),
                ),
              ],
            ),
          ),
        ),
        DemoSection(
          title: 'Espacio proporcional',
          description: 'Expanded y flex distribuyen el ancho disponible entre componentes.',
          child: const Row(
            children: [
              Expanded(
                child: _ColorBlock(label: '1/3', color: Color(0xff006d77)),
              ),
              Expanded(
                flex: 2,
                child: _ColorBlock(label: '2/3', color: Color(0xff315e9e)),
              ),
            ],
          ),
        ),
        DemoSection(
          title: 'Desplazamiento vertical',
          description: 'Esta página usa ListView para mantener accesibles todos los ejemplos.',
          child: const Text(
            'Desplázate para volver a los contenedores anteriores.',
          ),
        ),
      ],
    );
  }
}

class _ColorBlock extends StatelessWidget {
  const _ColorBlock({required this.label, required this.color});

  final String label;
  final Color color;

  @override
  Widget build(BuildContext context) {
    return Container(
      height: 44,
      color: color,
      alignment: Alignment.center,
      child: Text(label, style: const TextStyle(color: Colors.white)),
    );
  }
}
