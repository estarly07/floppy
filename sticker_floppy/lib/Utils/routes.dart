import 'package:flutter/cupertino.dart';
import 'package:sticker_floppy/Collections/ui/screens/detail_collection.dart';
import 'package:sticker_floppy/Sticker/ui/Screens/screens.dart';

Map<String, WidgetBuilder> routes = {
  "/": (_) => MainScreen(),
  "detail": (_) => DetailCollection()
};
