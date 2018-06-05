import {WeekDay} from "@angular/common";

export interface IOpenHours{

  id: number;
  openHour: string;
  closeHour: string;

}

export class OpenHours implements IOpenHours {
  id: number;
  openHour: string;
  closeHour: string;
  weekday: WeekDay;
  isSet: boolean = false;
  openingHour: MyTime = new MyTime();
  closingHour: MyTime = new MyTime();

  constructor(weekday: WeekDay) {
    this.weekday = weekday;
  }

  setClosingTime(hour: number, minute: number): boolean {
    if (MyTime.correctTime(hour, minute)) {
      return false;
    }
    const tmpTime = new MyTime();
    if (MyTime.timeCompare(this.openingHour, tmpTime) >= 1) {
      this.closingHour.setTime(hour, minute);
      return true;
    }
    return false;
  }

  setStartingTime(hour: number, minute: number): boolean {
    if (MyTime.correctTime(hour, minute)) {
      return false;
    }
    const tmpTime = new MyTime();
    if (MyTime.timeCompare(tmpTime, this.closingHour,) >= 1) {
      this.openingHour.setTime(hour, minute);
      return true;
    }
    return false;
  }

  public static fromJsonArray(json: string): OpenHours[] {
    let openHoursArray: OpenHours[] = [];
    for (let dayValue in
      Object.keys(WeekDay).filter(key => !isNaN(Number(WeekDay[key])))) {
      let weekday = WeekDay[dayValue].toUpperCase();
      if (json[weekday]) {
        let openHours = new OpenHours(weekday);
        openHours.isSet = true;
        openHours.openHour = json[weekday].openHour;
        openHours.closeHour = json[weekday].closeHour;
        openHoursArray.push(openHours);

      } else {
        openHoursArray.push(new OpenHours(weekday));
      }
    }
    return openHoursArray;

  }

  public static toJson(restaurantId: number, openHoursArray: OpenHours[]): string {
    let open: Map<WeekDay, string> = new Map<WeekDay, string>();
    let close: Map<WeekDay, string> = new Map<WeekDay, string>();
    openHoursArray.forEach(openHours => {
      open.set(openHours.weekday, openHours.openHour);
      close.set(openHours.weekday, openHours.closeHour);
    });
    return JSON.stringify({
      restaurantId: restaurantId,
      sunday: [open.get("SUNDAY"), close.get("SUNDAY")],
      monday: [open.get("MONDAY"), close.get("MONDAY")],
      tuesday: [open.get("TUESDAY"), close.get("WEDNESDAY")],
      wednesday: [open.get("WEDNESDAY"), close.get("WEDNESDAY")],
      thursday: [open.get("THURSDAY"), close.get("THURSDAY")],
      friday: [open.get("FRIDAY"), close.get("FRIDAY")],
      saturday: [open.get("SATURDAY"), close.get("SATURDAY")]
    });
  }


  public static fromJson(weekday: string, json: string): OpenHours {
    let openHours = new OpenHours(WeekDay[weekday]);
    if (json[weekday]) {
      openHours.isSet = true;
      openHours.openHour = json[weekday].openHour;
      openHours.closeHour = json[weekday].closeHour;
      return openHours;

    }
    return OpenHours;
  }
}

export class MyTime {
  hour: number = 0;
  minute: number = 0;

  constructor() {
  }

  setTime(hour: number, minute: number) {
    this.hour = hour;
    this.minute = minute;
  }

  public static timeCompare(time1: MyTime, time2: MyTime): number {
    if (time1.hour === time2.hour && time1.minute === time2.minute)
      return 0;
    else if (time1.hour > time2.hour ||
      (time1.hour === time2.hour && time1.minute >= time2.minute)) {
      return -1;
    }
    else {
      return 1;
    }
  }

  public static correctTime(hour: number, minute: number): boolean {
    if (hour >= 0 && hour < 24 && minute >= 0 && minute < 60) {
      return true;
    }
    return false;
  }

}
