import { BaseEntity } from './../../shared';

export class LeftoverDetails implements BaseEntity {
    constructor(
        public id?: number,
        public parentId?: number,
        public length?: number,
        public breadth?: number,
        public squareFeet?: number,
        public leftoverDetail?: BaseEntity,
    ) {
    }
}
