import 'package:flutter/material.dart';

import 'catalog_scope.dart';
import 'catalog_widgets.dart';

class TextInputsScreen extends StatefulWidget {
  const TextInputsScreen({super.key});

  @override
  State<TextInputsScreen> createState() => _TextInputsScreenState();
}

class _TextInputsScreenState extends State<TextInputsScreen> {
  final _validationKey = GlobalKey<FormState>();
  final _pendingController = TextEditingController();
  String _simpleText = '';
  String _password = '';
  String _number = '';
  String _email = '';
  String _phone = '';
  String _notes = '';
  String _search = '';
  String? _city;
  bool _showPassword = false;

  @override
  void dispose() {
    _pendingController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final store = CatalogScope.of(context);
    final results = ['Botón', 'Interruptor', 'Tarjeta', 'Deslizador']
        .where((item) => item.toLowerCase().contains(_search.toLowerCase()))
        .toList();

    return CatalogPage(
      children: [
        Text(
          'Prueba distintos tipos de captura y observa cómo cambia el teclado y la validación.',
          style: Theme.of(context).textTheme.bodyLarge,
        ),
        const SizedBox(height: 14),
        DemoSection(
          title: 'Campo simple',
          description: 'Captura un dato breve con una etiqueta clara.',
          child: TextField(
            onChanged: (value) => setState(() => _simpleText = value),
            decoration: InputDecoration(
              labelText: 'Nombre',
              helperText: _simpleText.isEmpty
                  ? 'Escribe tu nombre.'
                  : 'Valor: $_simpleText',
            ),
          ),
        ),
        DemoSection(
          title: 'Campo con validación',
          description: 'Muestra un error cuando el contenido no cumple la longitud requerida.',
          child: Form(
            key: _validationKey,
            child: TextFormField(
              autovalidateMode: AutovalidateMode.onUserInteraction,
              decoration: const InputDecoration(
                labelText: 'Usuario (mínimo 3 caracteres)',
              ),
              validator: (value) => (value ?? '').trim().length < 3
                  ? 'Escribe al menos 3 caracteres.'
                  : null,
            ),
          ),
        ),
        DemoSection(
          title: 'Contraseña',
          description: 'El icono final permite mostrar u ocultar el valor.',
          child: TextField(
            obscureText: !_showPassword,
            onChanged: (value) => setState(() => _password = value),
            decoration: InputDecoration(
              labelText: 'Contraseña',
              helperText: _password.isEmpty
                  ? 'Ingresa una contraseña.'
                  : 'Longitud: ${_password.length}',
              suffixIcon: IconButton(
                tooltip: _showPassword
                    ? 'Ocultar contraseña'
                    : 'Mostrar contraseña',
                onPressed: () => setState(() => _showPassword = !_showPassword),
                icon: Icon(
                  _showPassword
                      ? Icons.visibility_off_outlined
                      : Icons.visibility_outlined,
                ),
              ),
            ),
          ),
        ),
        DemoSection(
          title: 'Tipos de teclado',
          description:
              'Cada campo solicita el teclado adecuado para el tipo de dato.',
          child: Column(
            children: [
              TextField(
                keyboardType: TextInputType.number,
                onChanged: (value) => setState(() => _number = value),
                decoration: InputDecoration(
                  labelText: 'Número',
                  helperText: _number,
                ),
              ),
              TextField(
                keyboardType: TextInputType.emailAddress,
                onChanged: (value) => setState(() => _email = value),
                decoration: InputDecoration(
                  labelText: 'Correo electrónico',
                  helperText: _email,
                ),
              ),
              TextField(
                keyboardType: TextInputType.phone,
                onChanged: (value) => setState(() => _phone = value),
                decoration: InputDecoration(
                  labelText: 'Teléfono',
                  helperText: _phone,
                ),
              ),
            ],
          ),
        ),
        DemoSection(
          title: 'Campo multilínea',
          description: 'Sirve para comentarios y descripciones más extensas.',
          child: TextField(
            minLines: 3,
            maxLines: 5,
            onChanged: (value) => setState(() => _notes = value),
            decoration: InputDecoration(
              labelText: 'Comentarios',
              helperText: '${_notes.length} caracteres',
            ),
          ),
        ),
        DemoSection(
          title: 'Sugerencias',
          description:
              'Ofrece opciones frecuentes sin obligar a elegir una de ellas.',
          child: Autocomplete<String>(
            optionsBuilder: (value) {
              const cities = ['Ciudad de México', 'Guadalajara', 'Monterrey'];
              if (value.text.isEmpty) return cities;
              return cities.where(
                (city) => city.toLowerCase().contains(value.text.toLowerCase()),
              );
            },
            onSelected: (value) => setState(() => _city = value),
            fieldViewBuilder: (context, controller, focusNode, onSubmitted) =>
                TextField(
                  controller: controller,
                  focusNode: focusNode,
                  decoration: InputDecoration(
                    labelText: 'Ciudad',
                    helperText: _city == null
                        ? 'Elige o escribe una ciudad.'
                        : 'Seleccionada: $_city',
                  ),
                ),
          ),
        ),
        DemoSection(
          title: 'Barra de búsqueda',
          description: 'Filtra opciones conforme se escribe una consulta.',
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              TextField(
                onChanged: (value) => setState(() => _search = value),
                decoration: const InputDecoration(
                  labelText: 'Buscar componente',
                  prefixIcon: Icon(Icons.search),
                ),
              ),
              const SizedBox(height: 8),
              Text(results.isEmpty ? 'Sin coincidencias' : results.join(' · ')),
            ],
          ),
        ),
        DemoSection(
          title: 'Agregar a la lista',
          description:
              'Un texto válido se comparte con la sección Colecciones.',
          child: Column(
            children: [
              TextField(
                key: const Key('pending_text_input'),
                controller: _pendingController,
                onChanged: store.updatePendingText,
                decoration: const InputDecoration(labelText: 'Nuevo elemento'),
              ),
              const SizedBox(height: 10),
              FilledButton.icon(
                key: const Key('add_to_collection_button'),
                onPressed: () {
                  if (store.addPendingText()) {
                    _pendingController.clear();
                    ScaffoldMessenger.of(context).showSnackBar(
                      const SnackBar(
                        content: Text('Elemento agregado a la colección.'),
                      ),
                    );
                  } else {
                    ScaffoldMessenger.of(context).showSnackBar(
                      const SnackBar(
                        content: Text('Escribe al menos 3 caracteres.'),
                      ),
                    );
                  }
                },
                icon: const Icon(Icons.add),
                label: const Text('Agregar a la colección'),
              ),
            ],
          ),
        ),
      ],
    );
  }
}

class ActionsScreen extends StatefulWidget {
  const ActionsScreen({super.key});

  @override
  State<ActionsScreen> createState() => _ActionsScreenState();
}

class _ActionsScreenState extends State<ActionsScreen> {
  String _mode = 'Inactivo';
  bool _loading = false;

  void _show(String message) {
    ScaffoldMessenger.of(context)
        .showSnackBar(SnackBar(content: Text(message)));
  }

  Future<void> _simulateLoading() async {
    setState(() => _loading = true);
    await Future<void>.delayed(const Duration(milliseconds: 700));
    if (!mounted) return;
    setState(() => _loading = false);
    _show('La acción terminó.');
  }

  @override
  Widget build(BuildContext context) {
    return CatalogPage(
      children: [
        Text(
          'Cada acción genera una confirmación visible para demostrar su respuesta al toque.',
          style: Theme.of(context).textTheme.bodyLarge,
        ),
        const SizedBox(height: 14),
        DemoSection(
          title: 'Variantes de botón',
          description: 'Los botones relleno, contorno y texto expresan distinta jerarquía de acción.',
          child: Wrap(
            spacing: 8,
            runSpacing: 8,
            children: [
              FilledButton(
                onPressed: () => _show('Botón relleno pulsado.'),
                child: const Text('Relleno'),
              ),
              OutlinedButton(
                onPressed: () => _show('Botón con contorno pulsado.'),
                child: const Text('Contorno'),
              ),
              TextButton(
                onPressed: () => _show('Botón de texto pulsado.'),
                child: const Text('Texto'),
              ),
            ],
          ),
        ),
        DemoSection(
          title: 'Acciones con icono y FAB',
          description: 'Los iconos compactan acciones conocidas y los FAB destacan una acción principal.',
          child: Wrap(
            spacing: 12,
            runSpacing: 12,
            crossAxisAlignment: WrapCrossAlignment.center,
            children: [
              IconButton(
                tooltip: 'Eliminar',
                onPressed: () => _show('Acción de eliminar.'),
                icon: const Icon(Icons.delete_outline),
              ),
              FilledButton.icon(
                onPressed: () => _show('Acción de enviar.'),
                icon: const Icon(Icons.send_outlined),
                label: const Text('Enviar'),
              ),
              FloatingActionButton.small(
                heroTag: 'smallFab',
                tooltip: 'Agregar',
                onPressed: () => _show('FAB pulsado.'),
                child: const Icon(Icons.add),
              ),
              FloatingActionButton.extended(
                heroTag: 'extendedFab',
                onPressed: () => _show('FAB extendido pulsado.'),
                icon: const Icon(Icons.add),
                label: const Text('Crear'),
              ),
            ],
          ),
        ),
        DemoSection(
          title: 'Selector de alternancia',
          description:
              'Cambia un estado entre dos opciones mutuamente excluyentes.',
          child: SegmentedButton<String>(
            segments: const [
              ButtonSegment(
                value: 'Inactivo',
                label: Text('Inactivo'),
                icon: Icon(Icons.pause_circle_outline),
              ),
              ButtonSegment(
                value: 'Activo',
                label: Text('Activo'),
                icon: Icon(Icons.play_circle_outline),
              ),
            ],
            selected: {_mode},
            onSelectionChanged: (selection) {
              setState(() => _mode = selection.first);
              _show('Modo $_mode.');
            },
          ),
        ),
        DemoSection(
          title: 'Estados especiales',
          description: 'Un botón puede impedir acciones o informar que una tarea está en curso.',
          child: Wrap(
            spacing: 10,
            runSpacing: 10,
            crossAxisAlignment: WrapCrossAlignment.center,
            children: [
              const FilledButton(onPressed: null, child: Text('Deshabilitado')),
              FilledButton(
                onPressed: _loading ? null : _simulateLoading,
                child: _loading
                    ? const SizedBox(
                        width: 18,
                        height: 18,
                        child: CircularProgressIndicator(strokeWidth: 2),
                      )
                    : const Text('Simular carga'),
              ),
            ],
          ),
        ),
      ],
    );
  }
}
