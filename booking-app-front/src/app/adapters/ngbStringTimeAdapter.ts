import {NgbTimeStruct} from "@ng-bootstrap/ng-bootstrap";

export class NgbStringTimeAdapter {

  static fromModel(value: string): NgbTimeStruct {
    if (!value) {
      return null;
    }
    return {
      hour: +value.split(':')[0],
      minute: +value.split(':')[1],
      second: 0
    };
  }

  static toModel(time: NgbTimeStruct): string {
    if (!time) {
      return null;
    }
    return time.hour + ':' + time.minute;
  }
}
