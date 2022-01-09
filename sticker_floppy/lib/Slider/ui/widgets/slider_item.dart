import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/painting.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:sticker_floppy/Slider/bloc/slider_bloc.dart';
import 'package:sticker_floppy/Slider/model/slider.dart';
import 'package:sticker_floppy/Slider/ui/widgets/widgets.dart';
import 'package:sticker_floppy/Utils/services/shared_preferences.dart';

class SliderItem extends StatelessWidget {
  const SliderItem({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
        child: _Slide([
      SliderModel(
          image: "assets/one.png",
          title: "¿Que hacemos? \n🤔🤔🤔",
          description: "Te ahorramos el tiempo de guardar uno por uno",
          color: Colors.blue),
      SliderModel(
          image: "assets/dos.png",
          title: "¿Que te ofrecemos?\n👊",
          description:
              "Te ofrecemos una cantidad de stickeres \npara que no pierdas tu tiempo buscandolos",
          color: Colors.purple),
      SliderModel(
          image: "assets/floppy.png",
          title: "Disfruta de la App!!!\n🥳 🥳 🥳 ",
          description:
              "Gracias por descargar nuestra app \nEsperamos que la disfrutes",
          color: Colors.pink),
    ]));
  }
}

class _Slide extends StatefulWidget {
  List<SliderModel> sliders;

  _Slide(this.sliders);
  @override
  State<_Slide> createState() => _SlideState();
}

class _SlideState extends State<_Slide> {
  late final PageController pageController;

  @override
  void initState() {
    pageController = PageController();
    final provider = BlocProvider.of<SliderBloc>(context, listen: false);
    pageController.addListener(() {
      provider.add(ChangeSliderPageEvent(pageController.page!));
    });
    super.initState();
  }

  @override
  void dispose() {
    pageController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final size = MediaQuery.of(context).size;
    return BlocBuilder<SliderBloc, SliderState>(
      builder: (context, state) {
        return Stack(
          children: [
            Container(
              child: PageView(
                  controller: pageController,
                  children: widget.sliders.map((e) => Slider(e)).toList()),
            ),
            Align(
              alignment: Alignment.bottomLeft,
              child: Container(
                margin: EdgeInsets.only(bottom: size.height * 0.1),
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    Flexible(
                      flex: 1,
                      child: Container(
                        child: Dots(widget.sliders.length),
                      ),
                    ),
                    Flexible(
                        flex: 1,
                        child: Container(
                          margin: EdgeInsets.only(right: size.width * 0.1),
                          child: FloatingActionButton(
                            onPressed: () {
                              if (state.currentPage !=
                                  (widget.sliders.length - 1)) {
                                pageController.nextPage(
                                    duration: Duration(milliseconds: 250),
                                    curve: Curves.easeIn);
                              } else {
                                Preferences().firstTime();
                                Navigator.pushReplacementNamed(context, "/");
                              }
                            },
                            backgroundColor: Colors.amber[700],
                            child: Icon(Icons.arrow_right_alt),
                          ),
                        )),
                  ],
                ),
              ),
            )
          ],
        );
      },
    );
  }
}

class Slider extends StatelessWidget {
  SliderModel _slider;
  Slider(this._slider);

  @override
  Widget build(BuildContext context) {
    final size = MediaQuery.of(context).size;
    return Container(
        width: double.infinity,
        height: double.infinity,
        child: Stack(
          children: [
            Container(
              width: double.infinity,
              height: double.infinity,
              color: _slider.color,
            ),
            Container(
              child: Column(
                children: [
                  Flexible(
                      flex: 1,
                      child: DecorationCircles(
                        size: size,
                        image: _slider.image,
                      )),
                  Flexible(
                    flex: 1,
                    child: Container(
                      margin:
                          EdgeInsets.symmetric(horizontal: size.width * 0.05),
                      child: Column(
                        children: [
                          Text(
                            _slider.title,
                            textAlign: TextAlign.center,
                            style: TextStyle(
                                color: Colors.white,
                                fontWeight: FontWeight.bold,
                                fontSize: size.height * 0.04),
                          ),
                          Container(
                            margin: EdgeInsets.symmetric(
                                horizontal: size.width * 0.05,
                                vertical: size.height * 0.02),
                            child: Text(_slider.description,
                                textAlign: TextAlign.center,
                                style: TextStyle(
                                    color: Colors.white,
                                    fontSize: size.height * 0.02)),
                          ),
                        ],
                      ),
                    ),
                  )
                ],
              ),
            )
          ],
        ));
  }
}
