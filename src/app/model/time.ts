// import * as moment from "moment";
//
// export class Time {
//
//   openingHour: MyTime = new MyTime();
//   closingHour: MyTime = new MyTime();
//
//   time: moment.Moment;
//
//   constructor(time: string){
//     this.time = this.parseTime(time);
//   }
//
//   private parseTime(time: string): moment.Moment {
//     return moment(time);
//   }
//
//   public setTime(time: string) {
//     const timeValues: string[] = time.split(":");
//     this.time.set('second', +timeValues[0]);
//     this.time.set('minute', +timeValues[1]);
//     this.time.set('hour', +timeValues[2]);
//   }
//
//   public getTime(): string{
//     return this.time.format("HH:mm:ss");
//   }
// }
//
// export class MyTime {
//   hour: number = 0;
//   minute: number = 0;
//
//   constructor() {
//   }
//
//   setTime(hour: number, minute: number) {
//     this.hour = hour;
//     this.minute = minute;
//   }
//
//   public static timeCompare(time1: MyTime, time2: MyTime): number {
//     if (time1.hour === time2.hour && time1.minute === time2.minute)
//       return 0;
//     else if (time1.hour > time2.hour ||
//       (time1.hour === time2.hour && time1.minute >= time2.minute)) {
//       return -1;
//     }
//     else {
//       return 1;
//     }
//   }
//
//   public static correctTime(hour: number, minute: number): boolean {
//     if (hour >= 0 && hour < 24 && minute >= 0 && minute < 60) {
//       return true;
//     }
//     return false;
//   }
//   setClosingTime(hour: number, minute: number): boolean {
//     if (MyTime.correctTime(hour, minute)) {
//       return false;
//     }
//     const tmpTime = new MyTime();
//     if (MyTime.timeCompare(this.openingHour, tmpTime) >= 1) {
//       this.closingHour.setTime(hour, minute);
//       return true;
//     }
//     return false;
//   }
//
//   setStartingTime(hour: number, minute: number): boolean {
//     if (MyTime.correctTime(hour, minute)) {
//       return false;
//     }
//     const tmpTime = new MyTime();
//     if (MyTime.timeCompare(tmpTime, this.closingHour,) >= 1) {
//       this.openingHour.setTime(hour, minute);
//       return true;
//     }
//     return false;
//   }
//
//
// }
