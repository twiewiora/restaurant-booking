import {NgbTimeStruct} from "@ng-bootstrap/ng-bootstrap";
import {NgbStringTimeAdapter} from "../adapters/ngbStringTimeAdapter";

export interface IOpenHours {
  id: number;
  openHour: string;
  closeHour: string;
  isClose: boolean;
}

export enum Weekday {
  Sunday = "SUNDAY",
  Monday = "MONDAY",
  Tuesday = "TUESDAY",
  Wednesday = "WEDNESDAY",
  Thursday = "THURSDAY",
  Friday = "FRIDAY",
  Saturday = "SATURDAY"
}

export class OpenHours{

  id: number;
  openHour: NgbTimeStruct;
  closeHour: NgbTimeStruct;
  weekday: Weekday;
  isClose: boolean = true;

  constructor() {
  }

  getOpeningHourMinute(): number {
    return this.openHour.minute;
  }

  getOpeningHourHour(): number {
    return this.openHour.hour;
  }

  getClosingHourHour(): number {
    return this.closeHour.hour;
  }


  getClosingHourMinute(): number {
    return this.closeHour.minute;
  }


  public static createNewOpenHoursWeek(): Map<Weekday, OpenHours> {
    let openHoursWeek: Map<Weekday, OpenHours> = new Map<Weekday, OpenHours>();
    for (let dayValue in Weekday) {
      let weekday = dayValue.toUpperCase();
      let openHours = new OpenHours();
      openHours.weekday = <Weekday>weekday;
      openHoursWeek.set(<Weekday>weekday, openHours);
    }
    return openHoursWeek;
  }

  public static fromJsonArray(json: string): Map<Weekday, OpenHours> {
    let openHoursWeek: Map<Weekday, OpenHours> = new Map<Weekday, OpenHours>();
    for (let dayValue in Weekday) {
      let weekday = dayValue.toUpperCase();
      openHoursWeek.set(<Weekday>weekday, OpenHours.fromJson(weekday, json[weekday]));
    }
    return openHoursWeek;
  }

  public static fromJson(weekday: string, json: IOpenHours): OpenHours {
    let openHours = new OpenHours();
    openHours.weekday = <Weekday>weekday;
    openHours.openHour = NgbStringTimeAdapter.fromModel(json.openHour);
    openHours.closeHour = NgbStringTimeAdapter.fromModel(json.closeHour);
    openHours.isClose = json.isClose;

    return openHours;
  }

  public static toJson(openHoursWeek: Map<Weekday, OpenHours>): string {
    return JSON.stringify({
      sunday: [NgbStringTimeAdapter.toModel(openHoursWeek.get(Weekday.Sunday).openHour), NgbStringTimeAdapter.toModel(openHoursWeek.get(Weekday.Sunday).closeHour), openHoursWeek.get(Weekday.Sunday).isClose],
      monday: [NgbStringTimeAdapter.toModel(openHoursWeek.get(Weekday.Monday).openHour), NgbStringTimeAdapter.toModel(openHoursWeek.get(Weekday.Monday).closeHour), openHoursWeek.get(Weekday.Monday).isClose],
      tuesday: [NgbStringTimeAdapter.toModel(openHoursWeek.get(Weekday.Tuesday).openHour), NgbStringTimeAdapter.toModel(openHoursWeek.get(Weekday.Tuesday).closeHour), openHoursWeek.get(Weekday.Tuesday).isClose],
      wednesday: [NgbStringTimeAdapter.toModel(openHoursWeek.get(Weekday.Wednesday).openHour), NgbStringTimeAdapter.toModel(openHoursWeek.get(Weekday.Wednesday).closeHour), openHoursWeek.get(Weekday.Wednesday).isClose],
      thursday: [NgbStringTimeAdapter.toModel(openHoursWeek.get(Weekday.Thursday).openHour), NgbStringTimeAdapter.toModel(openHoursWeek.get(Weekday.Thursday).closeHour), openHoursWeek.get(Weekday.Thursday).isClose],
      friday: [NgbStringTimeAdapter.toModel(openHoursWeek.get(Weekday.Friday).openHour), NgbStringTimeAdapter.toModel(openHoursWeek.get(Weekday.Friday).closeHour), openHoursWeek.get(Weekday.Friday).isClose],
      saturday: [NgbStringTimeAdapter.toModel(openHoursWeek.get(Weekday.Saturday).openHour), NgbStringTimeAdapter.toModel(openHoursWeek.get(Weekday.Saturday).closeHour), openHoursWeek.get(Weekday.Saturday).isClose]
    });
  }
}
