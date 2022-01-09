import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:sticker_floppy/Collections/bloc/collections_bloc.dart';
import 'package:sticker_floppy/Utils/routes.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MultiBlocProvider(
      providers: [
        BlocProvider(create: (_) => CollectionsBloc()),
      ],
      child: MaterialApp(
        title: 'Material App',
        routes: routes,
        initialRoute: "splash",
        theme: ThemeData.light()
            .copyWith(scaffoldBackgroundColor: Colors.grey[100]),
      ),
    );
  }
}
