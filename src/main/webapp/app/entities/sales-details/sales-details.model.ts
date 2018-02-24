import { BaseEntity } from './../../shared';

export class SalesDetails implements BaseEntity {
    constructor(
        public id?: number,
        public length?: number,
        public breadth?: number,
        public squareFeet?: number,
        public saleDetail?: BaseEntity,
    ) {
    }
}
