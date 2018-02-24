/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';

import { InventoryTestModule } from '../../../test.module';
import { ProductDetailsDetailComponent } from '../../../../../../main/webapp/app/entities/product-details/product-details-detail.component';
import { ProductDetailsService } from '../../../../../../main/webapp/app/entities/product-details/product-details.service';
import { ProductDetails } from '../../../../../../main/webapp/app/entities/product-details/product-details.model';

describe('Component Tests', () => {

    describe('ProductDetails Management Detail Component', () => {
        let comp: ProductDetailsDetailComponent;
        let fixture: ComponentFixture<ProductDetailsDetailComponent>;
        let service: ProductDetailsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [InventoryTestModule],
                declarations: [ProductDetailsDetailComponent],
                providers: [
                    ProductDetailsService
                ]
            })
            .overrideTemplate(ProductDetailsDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ProductDetailsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProductDetailsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new ProductDetails(123)));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.productDetails).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
