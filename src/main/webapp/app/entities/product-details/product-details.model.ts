import { BaseEntity } from './../../shared';

export class ProductDetails implements BaseEntity {
    constructor(
        public id?: number,
        public length?: number,
        public breadth?: number,
        public squareFeet?: number,
        public sold?: string,
        public productDetail?: BaseEntity,
    ) {
    }
}
