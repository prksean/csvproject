import React from 'react';
import ReactDOM from 'react-dom';
import CsvUploader from './csv-uploader-component';
import renderer from 'react-test-renderer';
import {fireEvent, waitFor} from "@testing-library/dom";
import {act, render, screen} from "@testing-library/react";
import axios from "axios";
import MockAdapter from 'axios-mock-adapter';
import CsvUploaderService from "../service/csv-uploader-service";

beforeEach(() => jest.clearAllMocks());

describe('CSV Upload', () => {
    it('renders without crashing', () => {
        const div = document.createElement('div');
        ReactDOM.render(<CsvUploader/>, div);
        ReactDOM.unmountComponentAtNode(div);
    });

    it('renders correctly', () => {
        const tree = renderer.create(<CsvUploader/>).toJSON();
        expect(tree).toMatchSnapshot();
    });

    it("should drop csv file", async () => {
        const file = new File([], "test.csv", {type: 'text/csv'});
        const data = mockData([file])
        const ui = (
            <CsvUploader/>
        )

        const {rerender} = render(ui)
        const inputElement = screen.getByTestId("drop-input");

        dispatchEvt(inputElement, "drop", data);
        await flushPromises(rerender, ui);
    })

    it("should drop non-csv file", async () => {
        const nonCsvFile = new File([], "test.txt", {type: 'text/txt'});
        const nonCsvData = mockData([nonCsvFile])
        const ui = (
            <CsvUploader/>
        )

        const {rerender} = render(ui)
        const inputElement = screen.getByTestId("drop-input");
        dispatchEvt(inputElement, "drop", nonCsvData);
        await flushPromises(rerender, ui);
    });

    jest.mock('axios');

    it("200 test", async () => {
        const formdata = {};
        const instance = axios.create();
        const mock = new MockAdapter(instance);
        const mockData = {code: 200, message: "Success"};
        const event = jest.fn()

        //mocking
        mock
            .onPost('/csv/upload', event, formdata)
            .reply(200, mockData);

        const response = await CsvUploaderService(formdata, event);
        done();
        expect(response.status).toBe(200);
    });

    async function flushPromises(rerender, ui) {
        await act(() => waitFor(() => rerender(ui)))
    }

    function dispatchEvt(node, type, data) {
        const event = new Event(type, {bubbles: true})
        Object.assign(event, data)
        fireEvent.drop(node, event)
    }

    function mockData(files) {
        return {
            dataTransfer: {
                files,
                items: files.map(file => ({
                    kind: 'file',
                    type: file.type,
                    getAsFile: () => file
                })),
                types: ['Files']
            }
        }
    }
});
