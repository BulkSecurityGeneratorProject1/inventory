/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { Headers } from '@angular/http';

import { InventoryTestModule } from '../../../test.module';
import { SalesDetailsComponent } from '../../../../../../main/webapp/app/entities/sales-details/sales-details.component';
import { SalesDetailsService } from '../../../../../../main/webapp/app/entities/sales-details/sales-details.service';
import { SalesDetails } from '../../../../../../main/webapp/app/entities/sales-details/sales-details.model';

describe('Component Tests', () => {

    describe('SalesDetails Management Component', () => {
        let comp: SalesDetailsComponent;
        let fixture: ComponentFixture<SalesDetailsComponent>;
        let service: SalesDetailsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [InventoryTestModule],
                declarations: [SalesDetailsComponent],
                providers: [
                    SalesDetailsService
                ]
            })
            .overrideTemplate(SalesDetailsComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SalesDetailsComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SalesDetailsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new Headers();
                headers.append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of({
                    json: [new SalesDetails(123)],
                    headers
                }));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.salesDetails[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
