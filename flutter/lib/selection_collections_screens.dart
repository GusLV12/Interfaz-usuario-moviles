import 'package:flutter/material.dart';

import 'catalog_scope.dart';
import 'catalog_store.dart';
import 'catalog_widgets.dart';

class SelectionScreen extends StatefulWidget {
  const SelectionScreen({super.key});

  @override
  State<SelectionScreen> createState() => _SelectionScreenState();
}

class _SelectionScreenState extends State<SelectionScreen> {
  bool? _checked;
  String _choice = 'Opción A';
  bool _notifications = true;
  double _value = 45;
  RangeValues _range = const RangeValues(25, 75);
  String _theme = 'Sistema';
  DateTime? _date;
  TimeOfDay? _time;
  final Set<String> _filters = {};

  @override
  Widget build(BuildContext context) {
    return CatalogPage(
      children: [
        Text(
          'Elige valores y observa los estados individuales, excluyentes y de rango.',
          style: Theme.of(context).textTheme.bodyLarge,
        ),
        const SizedBox(height: 14),
        DemoSection(
          title: 'Casilla de verificación',
          description:
              'Puede estar marcada, desmarcada o en estado indeterminado.',
          child: CheckboxListTile(
            contentPadding: EdgeInsets.zero,
            tristate: true,
            value: _checked,
            onChanged: (value) => setState(() => _checked = value),
            title: Text(
              _checked == null
                  ? 'Indeterminado'
                  : _checked!
                  ? 'Marcado'
                  : 'Desmarcado',
            ),
          ),
        ),
        DemoSection(
          title: 'Botones de opción',
          description: 'Sólo una opción del grupo puede estar activa a la vez.',
          child: RadioGroup<String>(
            groupValue: _choice,
            onChanged: (value) => setState(() => _choice = value!),
            child: Column(
              children: [
                for (final option in ['Opción A', 'Opción B', 'Opción C'])
                  RadioListTile<String>(
                    contentPadding: EdgeInsets.zero,
                    title: Text(option),
                    value: option,
                  ),
              ],
            ),
          ),
        ),
        DemoSection(
          title: 'Interruptor',
          description: 'Activa o desactiva una preferencia binaria.',
          child: SwitchListTile(
            contentPadding: EdgeInsets.zero,
            value: _notifications,
            onChanged: (value) => setState(() => _notifications = value),
            title: Text(
              _notifications
                  ? 'Notificaciones activas'
                  : 'Notificaciones desactivadas',
            ),
          ),
        ),
        DemoSection(
          title: 'Deslizadores',
          description:
              'Seleccionan un valor único o los límites de un intervalo.',
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text('Valor: ${_value.round()}'),
              Slider(
                value: _value,
                onChanged: (value) => setState(() => _value = value),
              ),
              Text('Rango: ${_range.start.round()} a ${_range.end.round()}'),
              RangeSlider(
                values: _range,
                onChanged: (value) => setState(() => _range = value),
              ),
            ],
          ),
        ),
        DemoSection(
          title: 'Lista desplegable',
          description:
              'Presenta opciones cuando el espacio disponible es limitado.',
          child: DropdownButtonFormField<String>(
            initialValue: _theme,
            decoration: const InputDecoration(
              labelText: 'Tema de demostración',
            ),
            items: ['Sistema', 'Claro', 'Oscuro']
                .map((item) => DropdownMenuItem(value: item, child: Text(item)))
                .toList(),
            onChanged: (value) => setState(() => _theme = value!),
          ),
        ),
        DemoSection(
          title: 'Fecha y hora',
          description: 'Abren selectores nativos del dispositivo.',
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Wrap(
                spacing: 8,
                runSpacing: 8,
                children: [
                  OutlinedButton.icon(
                    onPressed: () async {
                      final date = await showDatePicker(
                        context: context,
                        firstDate: DateTime(2020),
                        lastDate: DateTime(2035),
                        initialDate: _date ?? DateTime.now(),
                      );
                      if (date != null) setState(() => _date = date);
                    },
                    icon: const Icon(Icons.calendar_today_outlined),
                    label: const Text('Fecha'),
                  ),
                  OutlinedButton.icon(
                    onPressed: () async {
                      final time = await showTimePicker(
                        context: context,
                        initialTime: _time ?? TimeOfDay.now(),
                      );
                      if (time != null) setState(() => _time = time);
                    },
                    icon: const Icon(Icons.schedule_outlined),
                    label: const Text('Hora'),
                  ),
                ],
              ),
              const SizedBox(height: 8),
              Text(
                '${_date == null ? 'Sin fecha' : '${_date!.day}/${_date!.month}/${_date!.year}'} · ${_time?.format(context) ?? 'Sin hora'}',
              ),
            ],
          ),
        ),
        DemoSection(
          title: 'Chips de filtro',
          description:
              'Permiten activar varias categorías de manera independiente.',
          child: Wrap(
            spacing: 8,
            runSpacing: 8,
            children: [
              for (final filter in ['Diseño', 'Código', 'Favorito'])
                FilterChip(
                  label: Text(filter),
                  selected: _filters.contains(filter),
                  onSelected: (selected) => setState(
                    () => selected
                        ? _filters.add(filter)
                        : _filters.remove(filter),
                  ),
                ),
            ],
          ),
        ),
      ],
    );
  }
}

class CollectionsScreen extends StatelessWidget {
  const CollectionsScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return DefaultTabController(
      length: 2,
      child: Column(
        children: [
          const TabBar(
            tabs: [
              Tab(text: 'Lista'),
              Tab(text: 'Cuadrícula'),
            ],
          ),
          Expanded(
            child: TabBarView(
              children: [
                _CollectionList(store: CatalogScope.of(context)),
                _CollectionGrid(store: CatalogScope.of(context)),
              ],
            ),
          ),
        ],
      ),
    );
  }
}

class _CollectionList extends StatelessWidget {
  const _CollectionList({required this.store});

  final CatalogStore store;

  @override
  Widget build(BuildContext context) {
    if (store.items.isEmpty) return _EmptyCollection(store: store);
    final grouped = <String, List<CatalogItem>>{};
    for (final item in store.items) {
      grouped.putIfAbsent(item.category, () => []).add(item);
    }

    return RefreshIndicator(
      onRefresh: () async => store.reset(),
      child: ListView(
        padding: const EdgeInsets.all(16),
        children: [
          Text(
            'Desliza un elemento para eliminarlo, tócalo para ver el detalle y arrastra hacia abajo para restaurar la colección.',
            style: Theme.of(context).textTheme.bodySmall,
          ),
          const SizedBox(height: 10),
          for (final entry in grouped.entries) ...[
            Padding(
              padding: const EdgeInsets.only(top: 10, bottom: 4),
              child: Text(
                entry.key,
                style: Theme.of(context).textTheme.titleSmall,
              ),
            ),
            for (final item in entry.value)
              _DismissibleCatalogItem(item: item, store: store),
          ],
          const SizedBox(height: 12),
          OutlinedButton.icon(
            onPressed: store.clear,
            icon: const Icon(Icons.delete_sweep_outlined),
            label: const Text('Vaciar colección'),
          ),
        ],
      ),
    );
  }
}

class _CollectionGrid extends StatelessWidget {
  const _CollectionGrid({required this.store});

  final CatalogStore store;

  @override
  Widget build(BuildContext context) {
    if (store.items.isEmpty) return _EmptyCollection(store: store);
    return RefreshIndicator(
      onRefresh: () async => store.reset(),
      child: GridView.builder(
        padding: const EdgeInsets.all(16),
        gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
          crossAxisCount: 2,
          mainAxisSpacing: 10,
          crossAxisSpacing: 10,
          childAspectRatio: 1.35,
        ),
        itemCount: store.items.length,
        itemBuilder: (context, index) {
          final item = store.items[index];
          return Card(
            clipBehavior: Clip.antiAlias,
            child: InkWell(
              onTap: () => _showDetail(context, item),
              child: Padding(
                padding: const EdgeInsets.all(12),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    const Icon(Icons.widgets_outlined),
                    const SizedBox(height: 8),
                    Text(
                      item.title,
                      style: Theme.of(context).textTheme.titleSmall,
                      maxLines: 2,
                      overflow: TextOverflow.ellipsis,
                    ),
                    Text(
                      item.category,
                      style: Theme.of(context).textTheme.bodySmall,
                    ),
                  ],
                ),
              ),
            ),
          );
        },
      ),
    );
  }
}

class _DismissibleCatalogItem extends StatelessWidget {
  const _DismissibleCatalogItem({required this.item, required this.store});

  final CatalogItem item;
  final CatalogStore store;

  @override
  Widget build(BuildContext context) {
    return Dismissible(
      key: ValueKey(item.id),
      direction: DismissDirection.endToStart,
      background: Container(
        alignment: Alignment.centerRight,
        padding: const EdgeInsets.only(right: 20),
        color: Theme.of(context).colorScheme.errorContainer,
        child: Icon(
          Icons.delete_outline,
          color: Theme.of(context).colorScheme.onErrorContainer,
        ),
      ),
      onDismissed: (_) {
        final removed = store.remove(item.id);
        if (removed == null) return;
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Text('${removed.title} eliminado.'),
            action: SnackBarAction(
              label: 'Deshacer',
              onPressed: () => store.restore(removed),
            ),
          ),
        );
      },
      child: ListTile(
        leading: const Icon(Icons.drag_indicator_outlined),
        title: Text(item.title),
        subtitle: Text(item.category),
        trailing: const Icon(Icons.chevron_right),
        onTap: () => _showDetail(context, item),
      ),
    );
  }
}

class _EmptyCollection extends StatelessWidget {
  const _EmptyCollection({required this.store});

  final CatalogStore store;

  @override
  Widget build(BuildContext context) {
    return RefreshIndicator(
      onRefresh: () async => store.reset(),
      child: ListView(
        physics: const AlwaysScrollableScrollPhysics(),
        padding: const EdgeInsets.all(32),
        children: [
          const SizedBox(height: 90),
          Icon(
            Icons.inventory_2_outlined,
            size: 72,
            color: Theme.of(context).colorScheme.primary,
          ),
          const SizedBox(height: 16),
          Text(
            'No hay elementos',
            textAlign: TextAlign.center,
            style: Theme.of(context).textTheme.headlineSmall,
          ),
          const SizedBox(height: 8),
          const Text(
            'Agrega uno desde Entrada de texto o restaura la colección de ejemplo.',
            textAlign: TextAlign.center,
          ),
          const SizedBox(height: 16),
          FilledButton.icon(
            onPressed: store.reset,
            icon: const Icon(Icons.restore),
            label: const Text('Restaurar elementos'),
          ),
        ],
      ),
    );
  }
}

void _showDetail(BuildContext context, CatalogItem item) {
  showModalBottomSheet<void>(
    context: context,
    showDragHandle: true,
    builder: (context) => Padding(
      padding: const EdgeInsets.fromLTRB(24, 0, 24, 28),
      child: Column(
        mainAxisSize: MainAxisSize.min,
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(
            'Detalle del elemento',
            style: Theme.of(context).textTheme.titleLarge,
          ),
          const SizedBox(height: 8),
          Text(item.title),
          Text('Categoría: ${item.category}'),
        ],
      ),
    ),
  );
}
