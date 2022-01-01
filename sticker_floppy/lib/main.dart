import 'package:flutter/material.dart';
import 'package:sticker_floppy/Utils/routes.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Material App',
      routes: routes,
      initialRoute: "/",
      theme:
          ThemeData.light().copyWith(scaffoldBackgroundColor: Colors.grey[100]),
    );
  }
}
