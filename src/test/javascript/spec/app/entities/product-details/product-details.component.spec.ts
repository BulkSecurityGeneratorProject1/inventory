/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { Headers } from '@angular/http';

import { InventoryTestModule } from '../../../test.module';
import { ProductDetailsComponent } from '../../../../../../main/webapp/app/entities/product-details/product-details.component';
import { ProductDetailsService } from '../../../../../../main/webapp/app/entities/product-details/product-details.service';
import { ProductDetails } from '../../../../../../main/webapp/app/entities/product-details/product-details.model';

describe('Component Tests', () => {

    describe('ProductDetails Management Component', () => {
        let comp: ProductDetailsComponent;
        let fixture: ComponentFixture<ProductDetailsComponent>;
        let service: ProductDetailsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [InventoryTestModule],
                declarations: [ProductDetailsComponent],
                providers: [
                    ProductDetailsService
                ]
            })
            .overrideTemplate(ProductDetailsComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ProductDetailsComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProductDetailsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new Headers();
                headers.append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of({
                    json: [new ProductDetails(123)],
                    headers
                }));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.productDetails[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
