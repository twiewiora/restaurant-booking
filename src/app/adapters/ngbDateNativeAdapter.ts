import {NgbDateStruct} from "@ng-bootstrap/ng-bootstrap/datepicker/ngb-date-struct";
import {NgbDateAdapter} from "@ng-bootstrap/ng-bootstrap";
import {Injectable} from "@angular/core";

@Injectable()
export class NgbDateNativeAdapter extends NgbDateAdapter<Date> {

   fromModel(value: Date): NgbDateStruct {
     if (!value) {
       return null;
     }
     return {day: value.getDate(), month: value.getMonth() + 1, year: value.getFullYear()};
   }

    toModel(date: NgbDateStruct): Date {
      if (!date) {
        return null;
      }
      return new Date(date.year, date.month - 1, date.day);
    }
}
