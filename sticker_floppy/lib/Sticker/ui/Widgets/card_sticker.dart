import 'package:flutter/cupertino.dart';
import 'package:sticker_floppy/Sticker/model/sticker.dart';

class CardSticker extends StatelessWidget {
  final Sticker sticker;
  const CardSticker({Key? key, required this.sticker}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final size = MediaQuery.of(context).size;
    return Container(
      padding: EdgeInsets.all(5),
      width: size.width * 0.25,
      child: FadeInImage(
          height: size.height * 0.2,
          width: size.height * 0.2,
          fit: BoxFit.contain,
          placeholder: AssetImage('assets/no-image.jpg'),
          image: NetworkImage(sticker.picture)),
    );
  }
}
