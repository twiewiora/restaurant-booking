export interface IOpenHours {
  id: number;
  openHour: string;
  closeHour: string;
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
  openHour: string = '';
  closeHour: string = '';
  weekday: Weekday;

  constructor() {
  }

  public static createNewOpenHoursWeek(): Map<Weekday,OpenHours>{
    let openHoursWeek: Map<Weekday, OpenHours> = new Map<Weekday,OpenHours>();
    for (let dayValue in Weekday) {
      let weekday = dayValue.toUpperCase();
      let openHours = new OpenHours();
      openHours.weekday = <Weekday>weekday;
      openHoursWeek.set(<Weekday>weekday, openHours);
    }
    return openHoursWeek;
  }

  public static fromJsonArray(json: string): Map<Weekday, OpenHours> {
    let openHoursWeek: Map<Weekday, OpenHours> = new Map<Weekday,OpenHours>();
    for (let dayValue in Weekday) {
      let weekday = dayValue.toUpperCase();
      let openHours = new OpenHours();
      openHours.weekday = <Weekday>weekday;
      if (json[weekday]) {
        openHours.openHour = json[weekday].openHour.substr(0, 5);
        openHours.closeHour = json[weekday].closeHour.substr(0, 5);
      }
      openHoursWeek.set(<Weekday>weekday, openHours);
    }
    return openHoursWeek;
  }

  public static toJson(openHoursWeek: Map<Weekday,OpenHours>): string {
    return JSON.stringify({
      sunday: [openHoursWeek.get(Weekday.Sunday).openHour, openHoursWeek.get(Weekday.Sunday).closeHour],
      monday: [openHoursWeek.get(Weekday.Monday).openHour, openHoursWeek.get(Weekday.Monday).closeHour],
      tuesday: [openHoursWeek.get(Weekday.Tuesday).openHour, openHoursWeek.get(Weekday.Tuesday).closeHour],
      wednesday: [openHoursWeek.get(Weekday.Wednesday).openHour, openHoursWeek.get(Weekday.Wednesday).closeHour],
      thursday: [openHoursWeek.get(Weekday.Thursday).openHour, openHoursWeek.get(Weekday.Thursday).closeHour],
      friday: [openHoursWeek.get(Weekday.Friday).openHour, openHoursWeek.get(Weekday.Friday).closeHour],
      saturday: [openHoursWeek.get(Weekday.Saturday).openHour, openHoursWeek.get(Weekday.Saturday).closeHour]
    });
  }


  public static fromJson(weekday: string, json: string): OpenHours {
    let openHours = new OpenHours();
    openHours.weekday = <Weekday>weekday;
    if (json[weekday]) {
      openHours.openHour = json[weekday].openHour;
      openHours.closeHour = json[weekday].closeHour;
      return openHours;

    }
    return openHours;
  }


  isSet(): boolean{
    return !!this.closeHour && !!this.openHour;
  }
}
