import http from 'k6/http';
import { check, sleep, group } from 'k6';
import { Rate } from 'k6/metrics';

export let errorRate = new Rate('errors');

export let options = {
    stages: [
        { duration: '10s', target: 10 },
        { duration: '30s', target: 20 },
        { duration: '10s', target: 0 },
    ],
    thresholds: {
        errors: ['rate<0.01'],
        http_req_duration: ['p(95)<500'],
    },
};

const BASE_URL = 'http://localhost:8080/api';

export default function () {
    group('🟢 Public Endpoint', function () {
        const res = http.get(`${BASE_URL}/public`);
        const success = check(res, {
            'status is 200': (r) => r.status === 200,
        });
        if (!success) {
            console.warn(` Public endpoint failed with status ${res.status}`);
            errorRate.add(1);
        }
    });

    group('📘 Courses - Wildcard Search', function () {
        const res = http.get(`${BASE_URL}/courses/wildcard?subjectCode=COMP&catalogPattern=2**`);
        const success = check(res, {
            'status is 200 or empty list': (r) => r.status === 200,
        });
        if (!success) {
            console.warn(` Wildcard search failed with status ${res.status}`);
            errorRate.add(1);
        }
    });

    group('🎓 Degree Requirements', function () {
        const res = http.get(`${BASE_URL}/degree/requirement?degreeName=COMPENG&showTech=true`);
        const success = check(res, {
            'status is 200 or 404 (if not found)': (r) => r.status === 200 || r.status === 404,
        });
        if (!success) {
            console.warn(` Degree requirement fetch failed with status ${res.status}`);
            errorRate.add(1);
        }
    });

    group('⭐ Ratings - Summary', function () {
        const res = http.get(`${BASE_URL}/ratings/summary/COMPENG2TR4`);
        const success = check(res, {
            'status is 200 or 204 (no content)': (r) => r.status === 200 || r.status === 204,
        });
        if (!success) {
            console.warn(` Rating summary failed with status ${res.status}`);
            errorRate.add(1);
        }
    });

    group('📄 Transcript Get (mock user)', function () {
        const res = http.get(`${BASE_URL}/transcripts/demo123`);
        const success = check(res, {
            'status is 200 or 404 (not uploaded yet)': (r) => r.status === 200 || r.status === 404,
        });
        if (!success) {
            console.warn(` Transcript fetch failed with status ${res.status}`);
            errorRate.add(1);
        }
    });

    group('📬 Seat Alerts - Manual Check', function () {
        const res = http.get(`${BASE_URL}/seat-alerts/check?courseCode=COMPENG2TR4&term=2239`);
        const success = check(res, {
            'status is 200 or false boolean': (r) => r.status === 200,
        });
        if (!success) {
            console.warn(` Seat alert check failed with status ${res.status}`);
            errorRate.add(1);
        }
    });

    sleep(1); // pacing between iterations
}
