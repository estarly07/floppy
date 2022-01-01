import 'dart:convert';

import 'package:http/http.dart';
import 'package:sticker_floppy/Sticker/model/collection.dart';

class ServiceSticker {
  final baseUrl = "https://stickers-26fd9-default-rtdb.firebaseio.com";

  Future getAllStickers() async {
    final url = Uri.parse("$baseUrl/collections.json");
    final response = await get(url);
    final List<Collection> collections = [];
    final Map<String, dynamic> data = json.decode(response.body);
    print(data.toString());

    data.forEach((key, value) {
      collections.add(Collection.fromMap(value));
    });
  }
}
