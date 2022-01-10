import 'package:bloc/bloc.dart';
import 'package:meta/meta.dart';

part 'slider_event.dart';
part 'slider_state.dart';

class SliderBloc extends Bloc<SliderEvent, SliderState> {
  SliderBloc() : super(SliderInitial()) {
    on<ChangeSliderPageEvent>((event, emit) {
      emit(ChangePageState(event.currentPage));
    });
  }
}
