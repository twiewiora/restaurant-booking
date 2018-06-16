export interface IOpenHours {
  id: number;
  openHour: {hour: number, minute: number};
  closeHour: {hour: number, minute: number};
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

export class OpenHours implements IOpenHours {
  id: number;
  openHour: {hour: number, minute: number} =  {hour: 0, minute: 0};
  closeHour: {hour: number, minute: number} = {hour: 0, minute: 0};
  weekday: Weekday;
  isClosed: boolean = true;

  constructor() {
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
      let openHours = new OpenHours();
      openHours.weekday = <Weekday>weekday;
      if (json[weekday]) {
        openHours.openHour.hour = +json[weekday].openHour.substr(0, 2);
        openHours.openHour.minute = +json[weekday].openHour.substr(3, 2);
        openHours.closeHour.hour = +json[weekday].closeHour.substr(0, 2);
        openHours.closeHour.minute = +json[weekday].closeHour.substr(3, 2);
        openHours.isClosed = false;
      }
      openHoursWeek.set(<Weekday>weekday, openHours);
    }
    return openHoursWeek;
  }

  public static fromJson(weekday: string, json: string): OpenHours {
    let openHours = new OpenHours();
    openHours.weekday = <Weekday>weekday;
    if (json[weekday]) {
      openHours.openHour.hour = +json[weekday].openHour.substr(0, 2);
      openHours.openHour.minute = +json[weekday].openHour.substr(3, 2);
      openHours.closeHour.hour = +json[weekday].closeHour.substr(0, 2);
      openHours.closeHour.minute = +json[weekday].closeHour.substr(3, 2);
      openHours.isClosed = false;
    }
    return openHours;
  }

  public static toJson(openHoursWeek: Map<Weekday, OpenHours>): string {
    return JSON.stringify({
      sunday: [OpenHours.getTimeAsString(openHoursWeek.get(Weekday.Sunday).openHour), OpenHours.getTimeAsString(openHoursWeek.get(Weekday.Sunday).closeHour)],// openHoursWeek.get(Weekday.Sunday).isClosed()
      monday: [OpenHours.getTimeAsString(openHoursWeek.get(Weekday.Monday).openHour), OpenHours.getTimeAsString(openHoursWeek.get(Weekday.Monday).closeHour)],
      tuesday: [OpenHours.getTimeAsString(openHoursWeek.get(Weekday.Tuesday).openHour), OpenHours.getTimeAsString(openHoursWeek.get(Weekday.Tuesday).closeHour)],
      wednesday: [OpenHours.getTimeAsString(openHoursWeek.get(Weekday.Wednesday).openHour), OpenHours.getTimeAsString(openHoursWeek.get(Weekday.Wednesday).closeHour)],
      thursday: [OpenHours.getTimeAsString(openHoursWeek.get(Weekday.Thursday).openHour), OpenHours.getTimeAsString(openHoursWeek.get(Weekday.Thursday).closeHour)],
      friday: [OpenHours.getTimeAsString(openHoursWeek.get(Weekday.Friday).openHour), OpenHours.getTimeAsString(openHoursWeek.get(Weekday.Friday).closeHour)],
      saturday: [OpenHours.getTimeAsString(openHoursWeek.get(Weekday.Saturday).openHour), OpenHours.getTimeAsString(openHoursWeek.get(Weekday.Saturday).closeHour)]
    });
  }

  static getTimeAsString(time: any): string {
    return time.hour + ':' + time.minute;
  }

}
