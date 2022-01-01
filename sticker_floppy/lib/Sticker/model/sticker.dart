class Sticker {
  Sticker({
    required this.id,
    required this.picture,
  });

  String id;
  String picture;

  factory Sticker.fromMap(Map<String, dynamic> json) => Sticker(
        id: json["id"],
        picture: json["picture"],
      );
}
