import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { ProductDetailsComponent } from './product-details.component';
import { ProductDetailsDetailComponent } from './product-details-detail.component';
import { ProductDetailsPopupComponent } from './product-details-dialog.component';
import { ProductDetailsDeletePopupComponent } from './product-details-delete-dialog.component';

export const productDetailsRoute: Routes = [
    {
        path: 'product-details',
        component: ProductDetailsComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'inventoryApp.productDetails.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'product-details/:id',
        component: ProductDetailsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'inventoryApp.productDetails.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const productDetailsPopupRoute: Routes = [
    {
        path: 'product-details-new',
        component: ProductDetailsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'inventoryApp.productDetails.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'product-details/:id/edit',
        component: ProductDetailsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'inventoryApp.productDetails.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'product-details/:id/delete',
        component: ProductDetailsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'inventoryApp.productDetails.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
