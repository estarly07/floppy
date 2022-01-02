import 'dart:convert';

import 'package:http/http.dart';
import 'package:sticker_floppy/Collections/model/collection.dart';
import 'package:sticker_floppy/Utils/global.dart';

class CollectionService {
  Future<List<Collection>> getAllCollections() async {
    final url = Uri.parse("$baseUrl/collections.json");
    final response = await get(url);
    final List<Collection> collections = [];
    final Map<String, dynamic> data = json.decode(response.body);
    print(data.toString());

    data.forEach((key, value) {
      collections.add(Collection.fromMap(value));
    });
    return collections;
  }
}
