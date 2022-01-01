import 'package:flutter/cupertino.dart';

class CardSticker extends StatelessWidget {
  const CardSticker({Key? key}) : super(key: key);

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
          image: NetworkImage(
              "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRhXMBky8c3hSn1fDwaNRiW2sa-7fxZ8VH7wA&usqp=CAU")),
    );
  }
}
