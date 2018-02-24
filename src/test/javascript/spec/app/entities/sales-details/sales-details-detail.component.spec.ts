/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';

import { InventoryTestModule } from '../../../test.module';
import { SalesDetailsDetailComponent } from '../../../../../../main/webapp/app/entities/sales-details/sales-details-detail.component';
import { SalesDetailsService } from '../../../../../../main/webapp/app/entities/sales-details/sales-details.service';
import { SalesDetails } from '../../../../../../main/webapp/app/entities/sales-details/sales-details.model';

describe('Component Tests', () => {

    describe('SalesDetails Management Detail Component', () => {
        let comp: SalesDetailsDetailComponent;
        let fixture: ComponentFixture<SalesDetailsDetailComponent>;
        let service: SalesDetailsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [InventoryTestModule],
                declarations: [SalesDetailsDetailComponent],
                providers: [
                    SalesDetailsService
                ]
            })
            .overrideTemplate(SalesDetailsDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SalesDetailsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SalesDetailsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new SalesDetails(123)));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.salesDetails).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
