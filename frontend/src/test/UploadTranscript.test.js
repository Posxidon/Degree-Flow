// eslint-disable-next-line import/no-extraneous-dependencies
const { Builder, By, until } = require('selenium-webdriver');
// eslint-disable-next-line import/no-extraneous-dependencies
require('chromedriver');
// eslint-disable-next-line import/no-extraneous-dependencies
const chrome = require('selenium-webdriver/chrome');
const path = require('path');

// Set timeout for Jest (30 sec max)
// eslint-disable-next-line no-undef
jest.setTimeout(30000);

// eslint-disable-next-line no-undef
describe('E2E: Upload Transcript Flow', () => {
  let driver;

  // eslint-disable-next-line no-undef
  beforeAll(async () => {
    const options = new chrome.Options();
    options.addArguments('--headless', '--no-sandbox', '--disable-dev-shm-usage');

    driver = await new Builder()
      .forBrowser('chrome')
      .setChromeOptions(options)
      .build();

    await driver.get('http://localhost:3000/dashboard');
  });

  // eslint-disable-next-line no-undef
  afterAll(async () => {
    if (driver) await driver.quit();
  });

  // eslint-disable-next-line no-undef
  test('can upload a transcript and see program summary', async () => {
    const uploadBtn = await driver.wait(
      until.elementLocated(By.xpath("//button[text()='Upload Transcript']")),
      10000
    );
    await uploadBtn.click();

    const agreeBtn = await driver.wait(
      until.elementLocated(By.xpath("//button[text()='I Agree']")),
      10000
    );
    await agreeBtn.click();

    const fileInput = await driver.wait(
      until.elementLocated(By.css('input[type="file"]')),
      10000
    );

    const filePath = path.resolve(__dirname, 'test.pdf'); // ✅ ensure this file exists
    await fileInput.sendKeys(filePath);

    const submitBtn = await driver.wait(
      until.elementLocated(By.xpath("//button[text()='Submit']")),
      10000
    );
    await submitBtn.click();

    const programSummary = await driver.wait(
      until.elementLocated(By.xpath("//*[contains(text(),'Program Summary')]")),
      10000
    );

    const summaryText = await programSummary.getText();
    // eslint-disable-next-line no-undef
    expect(summaryText).toContain('Program Summary');
  });
});
