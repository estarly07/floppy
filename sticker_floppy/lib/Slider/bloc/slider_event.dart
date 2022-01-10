part of 'slider_bloc.dart';

@immutable
abstract class SliderEvent {}

class ChangeSliderPageEvent extends SliderEvent {
  final double currentPage;
  ChangeSliderPageEvent(this.currentPage);
}
