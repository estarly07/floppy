import 'package:flutter/cupertino.dart';
import 'package:share/share.dart';
import 'package:sticker_floppy/Sticker/model/sticker.dart';

class CardSticker extends StatelessWidget {
  final Sticker sticker;
  const CardSticker({Key? key, required this.sticker}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final size = MediaQuery.of(context).size;
    return GestureDetector(
      onTap: () {
        Share.share('{stickers: ["${sticker.toMap()["picture"]}"]}');
      },
      child: Container(
        width: size.width * 0.15,
        margin: EdgeInsets.symmetric(horizontal: 5),
        child: FadeInImage(
            fit: BoxFit.contain,
            placeholder: AssetImage('assets/no-image.png'),
            image: NetworkImage(sticker.picture)),
      ),
    );
  }
}
