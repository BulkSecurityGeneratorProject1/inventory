import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { InventoryCategoryModule } from './category/category.module';
import { InventoryProductModule } from './product/product.module';
import { InventoryProductDetailsModule } from './product-details/product-details.module';
import { InventorySalesDetailsModule } from './sales-details/sales-details.module';
import { InventoryLeftoverDetailsModule } from './leftover-details/leftover-details.module';
import { InventoryWasteDetailsModule } from './waste-details/waste-details.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        InventoryCategoryModule,
        InventoryProductModule,
        InventoryProductDetailsModule,
        InventorySalesDetailsModule,
        InventoryLeftoverDetailsModule,
        InventoryWasteDetailsModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class InventoryEntityModule {}
