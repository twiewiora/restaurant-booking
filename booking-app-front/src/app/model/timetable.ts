import {WeekDay} from "@angular/common";

export interface ITimeTable {
  //restaurantId: number;
  openingHours: Map<string, OpenDay>;

  // setDay(weekday: WeekDay, startHour: number, startMinute: number, endHour: number, endMinute: number);

  toJson(): string;
}

export class TimeTable implements ITimeTable {

  //restaurantId: number;

  openingHours: Map<string, OpenDay> = new Map<string, OpenDay>();


  constructor() {
  }

  // setDay(weekday: WeekDay, startHour: number, startMinute: number, endHour: number, endMinute: number): boolean {
  //   const startTime = new MyTime();
  //   const endTime = new MyTime();
  //   if (!startTime.setTime(startHour, startMinute) &&
  //     !endTime.setTime(startHour, startMinute) &&
  //     MyTime.timeCompare(startTime, endTime) >= 1) {
  //     return false;
  //   }
  //   let openDay = new OpenDay();
  //   openDay.closingHour = endTime;
  //   openDay.openingHour = startTime;
  //   this.openingHours.set(weekday, openDay)
  //   return true;
  // }

  toJson(): string {
    return JSON.stringify({
      //  restaurantId: this.restaurantId
    });
  }
}

export class OpenDay {
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

  public static fromJsonArray(json: string): OpenDay[] {
    let openDays: OpenDay[] = [];
    for (let dayValue in
      Object.keys(WeekDay).filter(key => !isNaN(Number(WeekDay[key])))) {
      let weekday = WeekDay[dayValue].toUpperCase();
      if (json[weekday]) {
        let openDay = new OpenDay(WeekDay[dayValue]);
        openDay.isSet = true;
        openDay.openHour = json[weekday].openHour;
        openDay.closeHour = json[weekday].closeHour;
        openDays.push(openDay);

      } else {
        openDays.push(new OpenDay(WeekDay[dayValue]));
      }
    }
    return openDays;

  }

  public static fromJson(weekday: string, json: string): OpenDay {
    let openDay = new OpenDay(WeekDay[weekday]);
    if (json[weekday]) {
      openDay.isSet = true;
      openDay.openHour = json[weekday].openHour;
      openDay.closeHour = json[weekday].closeHour;
      return openDay;

    }
    return openDay;
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
