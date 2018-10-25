import {Injectable} from "@angular/core";
import {DATE_FORMAT} from "../model/reservation";
import * as moment from "moment";
import {cloneDeep} from "lodash";

@Injectable()
export class UrlQueryConverter {

  static booleanKeys = ['addReservation', 'addTable', 'edit', 'create'];

  static dictionaryToQuery(dictionary: {}): string {
    let queryList = [];
    for (let key in dictionary) {
      queryList.push(`${key}=${dictionary[key]}`);
    }
    return queryList.join('&');
  }

  static fixQueryDictionary(queryParams: {}): {} {
    let dictionary = cloneDeep(queryParams);
    if (dictionary['date'] && moment(dictionary['date'], DATE_FORMAT).format(DATE_FORMAT) !== dictionary['date']) {
      delete dictionary['date'];
    }
    this.booleanKeys.forEach(key => {
      if (dictionary[key] && dictionary[key] !== 'true') {
        delete dictionary[key];
      }
    });
    return dictionary;
  }
}
