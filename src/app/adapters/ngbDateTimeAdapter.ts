import {NgbTimeStruct} from "@ng-bootstrap/ng-bootstrap";

export class NgbDateTimeAdapter {

  static fromModel(value: Date): NgbTimeStruct {
    if (!value) {
      return null;
    }
    return {
      hour: value.getHours(),
      minute: value.getMinutes(),
      second: value.getSeconds()
    };
  }

  static toModel(time: NgbTimeStruct): Date {
    if (!time) {
      return null;
    }
    let date: Date = new Date();
    date.setHours(time.hour, time.minute, time.second);
    return date;
  }
}
