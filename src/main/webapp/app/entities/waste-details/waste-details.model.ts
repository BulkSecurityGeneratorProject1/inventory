import { BaseEntity } from './../../shared';

export class WasteDetails implements BaseEntity {
    constructor(
        public id?: number,
        public length?: number,
        public breadth?: number,
        public squareFeet?: number,
        public wasteDetail?: BaseEntity,
    ) {
    }
}
