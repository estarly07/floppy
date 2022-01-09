import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:sticker_floppy/Collections/bloc/collections_bloc.dart';
import 'package:sticker_floppy/Slider/bloc/slider_bloc.dart';
import 'package:sticker_floppy/Utils/routes.dart';
import 'package:sticker_floppy/Utils/services/shared_preferences.dart';

late Preferences preferences;
void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  preferences = Preferences();
  await preferences.initPreferences();
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MultiBlocProvider(
      providers: [
        BlocProvider(create: (_) => CollectionsBloc()),
        BlocProvider(create: (_) => SliderBloc())
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
