import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { InventorySharedModule } from '../../shared';
import {
    ProductDetailsService,
    ProductDetailsPopupService,
    ProductDetailsComponent,
    ProductDetailsDetailComponent,
    ProductDetailsDialogComponent,
    ProductDetailsPopupComponent,
    ProductDetailsDeletePopupComponent,
    ProductDetailsDeleteDialogComponent,
    productDetailsRoute,
    productDetailsPopupRoute,
} from './';

const ENTITY_STATES = [
    ...productDetailsRoute,
    ...productDetailsPopupRoute,
];

@NgModule({
    imports: [
        InventorySharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        ProductDetailsComponent,
        ProductDetailsDetailComponent,
        ProductDetailsDialogComponent,
        ProductDetailsDeleteDialogComponent,
        ProductDetailsPopupComponent,
        ProductDetailsDeletePopupComponent,
    ],
    entryComponents: [
        ProductDetailsComponent,
        ProductDetailsDialogComponent,
        ProductDetailsPopupComponent,
        ProductDetailsDeleteDialogComponent,
        ProductDetailsDeletePopupComponent,
    ],
    providers: [
        ProductDetailsService,
        ProductDetailsPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class InventoryProductDetailsModule {}
