import 'dart:convert';

import 'package:sticker_floppy/Sticker/model/sticker.dart';

Collection stickerFromMap(String str) => Collection.fromMap(json.decode(str));

class Collection {
  Collection(
      {required this.nameCollection,
      required this.stickers,
      required this.frontPage});

  String nameCollection;
  String frontPage;
  List<Sticker> stickers;

  factory Collection.fromMap(Map<String, dynamic> json) {
    Map<String, dynamic> stickersMap = json["stickers"];
    List<Sticker> newStickers = [];
    stickersMap.forEach((key, value) {
      print("VALUE " + value.toString());
      newStickers.add(Sticker.fromMap(value));
    });
    return Collection(
        frontPage: json["front page"],
        nameCollection: json["nameCollection"],
        stickers: newStickers);
  }
}
