import {OpenHoursDay, Weekday} from "../model/open-hours2";
import * as moment from "moment";

export class OpenHoursDayConverter {
  fromJson(weekday: string, json: string): OpenHoursDay {
    let tmpOpenHoursDay = JSON.parse(json);
    let openHoursDay = new OpenHoursDay();
    openHoursDay.id = json['id'];
    openHoursDay.weekday = <Weekday> weekday;
    openHoursDay.closeHour = moment('11:11:11');
    openHoursDay.closeHour.format("HH:mm:ss");
    return openHoursDay;
  }


}

